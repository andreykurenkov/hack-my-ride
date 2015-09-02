from django.conf.urls import patterns, include, url
from views import *

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'nearest/$',
        NearestStopsListAPIView.as_view(), name='nearest stops'),
    
    url(r'nearest/agency$',
        NearestAgencyAPIView.as_view(), name='nearest agency'),
    
    url(r'agencies/$',
        AgencyListAPIView.as_view(), name='agencies'),

    url(r'agency/(?P<agency_id>[^/]+)/?$',
        AgencyDetailAPIView.as_view(), name='agency_detail'),

    url(r'agency/(?P<agency_id>[^/]+)/routes/?$',
        RouteListAPIView.as_view(), name='routes'),
    
    url(r'agency/(?P<agency_id>[^/]+)/routes/?(?P<r_type>[^/]+)$',
        TypedRouteListAPIView.as_view(), name='typed routes'),

    url(r'route/(?P<route_id>[^/]+)/?$',
        RouteDetailAPIView.as_view(), name='route_detail'),

    url(r'route/(?P<route_id>[^/]+)/stops/?$',
        RouteStopsListAPIView.as_view(), name='route stops'),
    
    url(r'stop/(?P<stop_id>[^/]+)/?$',
        StopDetailAPIView.as_view(), name='stop_detail'),

    url(r'stop/(?P<stop_id>[^/]+)/times/?$',
        StopTimesListAPIView.as_view(), name='stop times'),
    
    url(r'stop/(?P<stop_id>[^/]+)/stoptimeafter/?(?P<time>[^/]+)$',
        StopNextTimeAPIView.as_view(), name='stop time after'),
    
    url(r'stop_time/(?P<stop_time_id>[^/]+)/?$',
        StopTimeDetailAPIView.as_view(), name='stop_time_detail'),
    
    url(r'route/(?P<route_id>[^/]+)/trips/?$',
        TripListAPIView.as_view(), name='trips'),

    url(r'trip/(?P<trip_id>[^/]+)/?$',
        TripDetailAPIView.as_view(), name='trip_detail'),


)
