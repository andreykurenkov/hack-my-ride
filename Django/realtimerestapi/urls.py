from django.conf.urls import patterns, include, url
from views import *

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'stop/realtimemonitor/(?P<stop_id>[^/]+)/?$',
        StopRealTimeAPIView.as_view(), name='stop_realtime_detail'),
    url(r'trips/realtime/(?P<agency_name>[^/]+)/?$',
        TripsRealTimeUpdatesAPIView.as_view(), name='trips_realtime_update'),
    url(r'trip/realtime/(?P<trip_id>[^/]+)/?$',
        TripRealTimeUpdatesAPIView.as_view(), name='trip_realtime_update'),
    url(r'stop_time/realtime/(?P<stop_time_id>[^/]+)/?$',
        StopRealTimeUpdatesAPIView.as_view(), name='stop_time_realtime_update')
)
