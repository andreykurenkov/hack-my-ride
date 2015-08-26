from django.conf.urls import patterns, include, url
from views import *

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'agencies/$',
        AgencyListAPIView.as_view(), name='agencies'),
    url(r'agency/(?P<agency_id>[^/]+)/?$',
        AgencyDetailAPIView.as_view(), name='agency_detail'),
    url(r'routes/(?P<agency_id>[^/]+)/$',
        RouteListAPIView.as_view(), name='routes'),
    url(r'route/(?P<agency_id>[^/]+)/(?P<res_id>[^/]+)/?$',
        RouteDetailAPIView.as_view(), name='route_detail'),
    url(r'stops/(?P<agency_id>[^/]+)/$',
        StopListAPIView.as_view(), name='stops'),
    url(r'stop/(?P<agency_id>[^/]+)/(?P<res_id>[^/]+)/?$',
        StopDetailAPIView.as_view(), name='stop_detail'),
    url(r'stop/(?P<agency_id>[^/]+)/(?P<res_id>[^/]+)/times/$',
        StopDetailTimesAPIView.as_view(), name='stop_detail_times'),
    url(r'trips/(?P<agency_id>[^/]+)/$',
        TripListAPIView.as_view(), name='trips'),
    url(r'trip/(?P<agency_id>[^/]+)/(?P<res_id>[^/]+)/?$',
        TripDetailAPIView.as_view(), name='trip_detail'),
    url(r'services/(?P<agency_id>[^/]+)/$',
        ServiceListAPIView.as_view(), name='services'),
    url(r'service/(?P<agency_id>[]+)/(?P<res_id>[^/]+)/?$',
        ServiceDetailAPIView.as_view(), name='service_detail'),
)
