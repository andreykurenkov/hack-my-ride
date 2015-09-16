from django.conf.urls import patterns, include, url
from views import *

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'realtime/stops/(?P<agency_name>[^/]+)/?$',
        StopsWithRealTimeDataAPIView.as_view(), name='stops_with_realtime_data'),
    url(r'realtime/stopmonitor/(?P<stop_id>[^/]+)/?$',
        StopRealTimeAPIView.as_view(), name='stop_realtime_detail'),
    url(r'realtime/trips/(?P<agency_name>[^/]+)/?$',
        TripsRealTimeUpdatesAPIView.as_view(), name='trips_realtime_update'),
    url(r'realtime/trip/(?P<trip_id>[^/]+)/?$',
        TripRealTimeUpdatesAPIView.as_view(), name='trip_realtime_update'),
    url(r'realtime/stop_time/(?P<stop_time_id>[^/]+)/?$',
        StopRealTimeUpdatesAPIView.as_view(), name='stop_time_realtime_update')
)
