from django.conf.urls import patterns, url
from django.views.generic import DetailView, ListView
from multigtfs.models import (
    Agency, Block, Fare, FareRule, Feed, FeedInfo, Route, Service, ServiceDate,
    Shape, ShapePoint, Stop, StopTime, Trip, Zone)

urlpatterns = patterns('',
    url(r'about/$', 'transittimesbrowse.views.about', name='about'),
    url(r'(?P<agency_id>.+)/routes/$', 'transittimesbrowse.views.routes', name='routes'),
    url(r'(?P<agency_id>.+)/routes/$', 'transittimesbrowse.views.routes', name='routes'),
    url(r'(?P<agency_id>.+)/routes/(?P<route_id>.+)/stops/$', 'transittimesbrowse.views.stops', name='stops'),
    url(r'(?P<agency_id>.+)/routes/(?P<route_id>.+)/stops/(?P<stop_id>\d+)/$', 'transittimesbrowse.views.stop', name='stop'),
    url(r'(?P<agency_id>.+)/routes/(?P<route_id>.+)/stops/(?P<stop_id>\d+)/trip/(?P<trip_id>\d+)$', 'transittimesbrowse.views.trip', name='trip')
)
