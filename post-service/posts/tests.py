from django.test import TestCase
from django.urls import reverse
from rest_framework.test import APITestCase
from rest_framework import status
from .models import Post, Comment, Like, PostShare
import uuid


class PostModelTest(TestCase):
    """Test cases for Post model."""
    
    def setUp(self):
        self.post = Post.objects.create(
            author_id='test_user_123',
            content='This is a test post content'
        )
    
    def test_post_creation(self):
        """Test that a post can be created."""
        self.assertEqual(self.post.author_id, 'test_user_123')
        self.assertEqual(self.post.content, 'This is a test post content')
        self.assertFalse(self.post.is_deleted)
        self.assertIsNotNone(self.post.id)
    
    def test_post_likes_count(self):
        """Test likes count property."""
        # Create some likes
        Like.objects.create(post=self.post, user_id='user1', is_active=True)
        Like.objects.create(post=self.post, user_id='user2', is_active=True)
        Like.objects.create(post=self.post, user_id='user3', is_active=False)
        
        self.assertEqual(self.post.likes_count, 2)
    
    def test_post_comments_count(self):
        """Test comments count property."""
        # Create some comments
        Comment.objects.create(post=self.post, author_id='user1', content='Comment 1')
        Comment.objects.create(post=self.post, author_id='user2', content='Comment 2')
        Comment.objects.create(post=self.post, author_id='user3', content='Comment 3', is_deleted=True)
        
        self.assertEqual(self.post.comments_count, 2)


class CommentModelTest(TestCase):
    """Test cases for Comment model."""
    
    def setUp(self):
        self.post = Post.objects.create(
            author_id='test_user_123',
            content='Test post'
        )
        self.comment = Comment.objects.create(
            post=self.post,
            author_id='commenter_123',
            content='This is a test comment'
        )
    
    def test_comment_creation(self):
        """Test that a comment can be created."""
        self.assertEqual(self.comment.post, self.post)
        self.assertEqual(self.comment.author_id, 'commenter_123')
        self.assertEqual(self.comment.content, 'This is a test comment')
        self.assertFalse(self.comment.is_deleted)
    
    def test_nested_comments(self):
        """Test nested comments (replies)."""
        reply = Comment.objects.create(
            post=self.post,
            author_id='replier_123',
            content='This is a reply',
            parent_comment=self.comment
        )
        
        self.assertEqual(reply.parent_comment, self.comment)
        self.assertIn(reply, self.comment.replies.all())


class LikeModelTest(TestCase):
    """Test cases for Like model."""
    
    def setUp(self):
        self.post = Post.objects.create(
            author_id='test_user_123',
            content='Test post'
        )
    
    def test_like_creation(self):
        """Test that a like can be created."""
        like = Like.objects.create(
            post=self.post,
            user_id='liker_123',
            is_active=True
        )
        
        self.assertEqual(like.post, self.post)
        self.assertEqual(like.user_id, 'liker_123')
        self.assertTrue(like.is_active)
    
    def test_unique_user_post_like(self):
        """Test that a user can only have one like per post."""
        Like.objects.create(post=self.post, user_id='user1', is_active=True)
        
        # Try to create another like for the same user and post
        like2 = Like.objects.create(post=self.post, user_id='user1', is_active=False)
        
        # Should update the existing like instead of creating a new one
        self.assertEqual(Like.objects.filter(post=self.post, user_id='user1').count(), 1)


class PostShareModelTest(TestCase):
    """Test cases for PostShare model."""
    
    def setUp(self):
        self.post = Post.objects.create(
            author_id='test_user_123',
            content='Test post'
        )
    
    def test_share_creation(self):
        """Test that a share can be created."""
        share = PostShare.objects.create(
            original_post=self.post,
            shared_by='sharer_123',
            share_message='Check out this post!'
        )
        
        self.assertEqual(share.original_post, self.post)
        self.assertEqual(share.shared_by, 'sharer_123')
        self.assertEqual(share.share_message, 'Check out this post!')


class PostAPITest(APITestCase):
    """Test cases for Post API endpoints."""
    
    def setUp(self):
        self.post = Post.objects.create(
            author_id='test_user_123',
            content='Test post content'
        )
    
    def test_get_posts_list(self):
        """Test getting list of posts."""
        url = reverse('post-list')
        response = self.client.get(url)
        
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(len(response.data['results']), 1)
    
    def test_get_single_post(self):
        """Test getting a single post."""
        url = reverse('post-detail', args=[self.post.id])
        response = self.client.get(url)
        
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data['id'], str(self.post.id))
        self.assertEqual(response.data['content'], self.post.content)
    
    def test_create_post(self):
        """Test creating a new post."""
        url = reverse('post-list')
        data = {
            'content': 'New test post content'
        }
        response = self.client.post(url, data, format='json')
        
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Post.objects.count(), 2)
    
    def test_update_post(self):
        """Test updating a post."""
        url = reverse('post-detail', args=[self.post.id])
        data = {
            'content': 'Updated post content'
        }
        response = self.client.patch(url, data, format='json')
        
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.post.refresh_from_db()
        self.assertEqual(self.post.content, 'Updated post content')
    
    def test_delete_post(self):
        """Test soft deleting a post."""
        url = reverse('post-detail', args=[self.post.id])
        response = self.client.delete(url)
        
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)
        self.post.refresh_from_db()
        self.assertTrue(self.post.is_deleted)
    
    def test_like_post(self):
        """Test liking a post."""
        url = reverse('post-like', args=[self.post.id])
        response = self.client.post(url)
        
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertTrue(response.data['liked'])
        self.assertEqual(response.data['likes_count'], 1)
    
    def test_share_post(self):
        """Test sharing a post."""
        url = reverse('post-share', args=[self.post.id])
        data = {
            'share_message': 'Check out this amazing post!'
        }
        response = self.client.post(url, data, format='json')
        
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(PostShare.objects.count(), 1)


class CommentAPITest(APITestCase):
    """Test cases for Comment API endpoints."""
    
    def setUp(self):
        self.post = Post.objects.create(
            author_id='test_user_123',
            content='Test post'
        )
        self.comment = Comment.objects.create(
            post=self.post,
            author_id='commenter_123',
            content='Test comment'
        )
    
    def test_get_comments_list(self):
        """Test getting list of comments."""
        url = reverse('comment-list')
        response = self.client.get(url)
        
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(len(response.data['results']), 1)
    
    def test_create_comment(self):
        """Test creating a new comment."""
        url = reverse('comment-list')
        data = {
            'post': self.post.id,
            'content': 'New test comment'
        }
        response = self.client.post(url, data, format='json')
        
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Comment.objects.count(), 2)
    
    def test_get_comment_replies(self):
        """Test getting replies to a comment."""
        reply = Comment.objects.create(
            post=self.post,
            author_id='replier_123',
            content='This is a reply',
            parent_comment=self.comment
        )
        
        url = reverse('comment-replies', args=[self.comment.id])
        response = self.client.get(url)
        
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(len(response.data), 1)
        self.assertEqual(response.data[0]['id'], str(reply.id)) 