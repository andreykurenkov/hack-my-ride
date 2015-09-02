from django.conf.urls import patterns, include, url
from views import *

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'stop/realtime/(?P<stop_id>[^/]+)/?$',
        StopRealTimeAPIView.as_view(), name='stop_realtime_detail'),
    url(r'trips/realtime/(?P<agency_name>[^/]+)/?$',
        TripRealTimeUpdatesAPIView.as_view(), name='trips_realtime_update')
)
