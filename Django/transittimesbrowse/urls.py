from django.conf.urls import patterns, url
from django.views.generic import DetailView, ListView
from multigtfs.models import (
    Agency, Block, Fare, FareRule, Feed, FeedInfo, Route, Service, ServiceDate,
    Shape, ShapePoint, Stop, StopTime, Trip, Zone)

urlpatterns = patterns('',
    url(r'(?P<agency_id>.+)/routes/$', 'transittimesbrowse.views.routes', name='routes'),
    url(r'(?P<agency_id>.+)/routes/(?P<route_id>\d+)/stops$', 'transittimesbrowse.views.stops', name='stops')
)
