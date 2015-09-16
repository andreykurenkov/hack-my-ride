from django.views.generic import ListView
from multigtfs.models import (
    Agency, Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip)
from django.shortcuts import render_to_response
from django.template import RequestContext
from multigtfs.models.fields import Seconds
import datetime
def index(request):
    agencies = Agency.objects.order_by('name')
    template = 'index.html'
    if request.is_ajax():
        template = 'agency_list.html'
        search_text = request.GET['search']
        agencies = agencies.filter(name__icontains=search_text)
    context_instance=RequestContext(request,{'agencies':agencies})
    return render_to_response(template, context_instance)

def about(request):
    return render_to_response('about.html', RequestContext(request))

def routes(request,agency_id):
    template = 'routes_page.html'
    routes = Route.objects.filter(agency__agency_id=agency_id).order_by('long_name')
    agency = Agency.objects.all().get(agency_id=agency_id)
    longest_geom = None
    if request.is_ajax():
        template = 'routes_list.html'
        search_text = request.GET['search']
        routes = routes.filter(long_name__icontains=search_text)
    else:
        for route in routes:
            if route.geometry!=None and (longest_geom==None or len(route.geometry)>len(longest_geom)):
                longest_geom = route.geometry

    context_instance=RequestContext(request,{'agency':agency,'routes':routes,'geom':longest_geom})
    return render_to_response(template, context_instance)

def stops(request,agency_id,route_id):
    template = 'stops_page.html'
    agency = Agency.objects.get(agency_id=agency_id)
    route = Route.objects.get(route_id=route_id,agency__pk=agency.pk)
    trip_ids = Trip.objects.filter(route__id = route.pk).values_list("id")
    stop_ids =  StopTime.objects.filter(trip__id__in=trip_ids).values_list("stop__id")
    stops = Stop.objects.filter(id__in=stop_ids).distinct().order_by('name')
    if request.is_ajax():
        template = 'stops_list.html'
        search_text = request.GET['search']
        stops = stops.filter(name__icontains=search_text)
    context_instance=RequestContext(request,{'agency':agency,'route':route,'stops':stops})
    return render_to_response(template, context_instance)

def stop(request,agency_id,route_id,stop_id):
    template = 'stop_page.html'
    agency = Agency.objects.get(agency_id=agency_id)
    route = Route.objects.get(route_id=route_id,agency__pk=agency.pk)
    stop = Stop.objects.get(pk=stop_id)
    stop_times = []
    num = 3 
    if 'num' in request.GET:
        num = int(request.GET['num'])
    if 'time' in request.GET:
        time = Seconds(int(request.GET['time']))
        stop_times = stop.stoptime_set.filter(arrival_time__gt=time).order_by('arrival_time')
        weekday = datetime.datetime.today().weekday()
        weekdayname = ['monday','tuesday','wednesday','thursday','friday','saturday','sunday'][weekday]
        stop_times = [stoptime for stoptime in stop_times if getattr(stoptime.trip.service,weekdayname)]
        stop_times = stop_times[0:min(len(stop_times),num)]
    if request.is_ajax():
        template = 'stop_times_list.html'
    context_instance=RequestContext(request,{'agency':agency,'route':route,'stop':stop,'stop_times':stop_times})
    return render_to_response(template, context_instance)

def trip(request,agency_id,route_id,stop_id,trip_id):
    template = 'trip_page.html'
    agency = Agency.objects.get(agency_id=agency_id)
    route = Route.objects.get(route_id=route_id,agency__pk=agency.pk)
    trip = Trip.objects.get(pk=trip_id)
    stop_times = trip.stoptime_set.order_by('arrival_time')
    stop_ids =  StopTime.objects.filter(trip__id=trip_id).values_list("stop__id")
    stops = Stop.objects.filter(id__in=stop_ids).distinct().order_by('name')
    if 'search' in request.GET:
        search_text = request.GET['search']
        stops = stops.filter(name__icontains=search_text)
    if request.is_ajax():
        template = 'stops_list.html'
    context_instance=RequestContext(request,{'agency':agency,'route':route,'stops':stops,'stop_times':stop_times,'trip':trip})
    return render_to_response(template, context_instance)

