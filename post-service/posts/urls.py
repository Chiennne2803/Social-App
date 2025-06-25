from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views

router = DefaultRouter()
router.register(r'posts', views.PostViewSet, basename='post')
router.register(r'comments', views.CommentViewSet, basename='comment')
router.register(r'likes', views.LikeViewSet, basename='like')
router.register(r'shares', views.PostShareViewSet, basename='share')

urlpatterns = [
    path('', include(router.urls)),
] 