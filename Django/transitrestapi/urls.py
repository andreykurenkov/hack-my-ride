from django.conf.urls import patterns, include, url
from views import *

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'routes/$',
        RouteListAPIView.as_view(), name='fares'),
    url(r'route/(?P<pk>[^/]+)/?$',
        RouteDetailAPIView.as_view(), name='fare_detail'),
    url(r'stops/$',
        StopListAPIView.as_view(), name='stops'),
    url(r'stop/(?P<stop_id>[^/]+)/?$',
        StopDetailAPIView.as_view(), name='stop_detail'),
    url(r'stop/(?P<stop_id>[^/]+)/times?$',
        StopTimesListAPIView.as_view(), name='stop_detail_times'),
    url(r'trips/$',
        TripListAPIView.as_view(), name='trips'),
    url(r'trip/(?P<pk>[^/]+)/?$',
        TripDetailAPIView.as_view(), name='trip_detail'),
    url(r'services/$',
        ServiceListAPIView.as_view(), name='services'),
    url(r'service/(?P<pk>[^/]+)/?$',
        ServiceDetailAPIView.as_view(), name='service_detail'),
)
