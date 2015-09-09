from django.db.models import Max
from rest_framework import generics
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from multigtfs.models import (
    Agency, Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip)
from multigtfs.models.fields import Seconds
from transitrestapi.serializers import *
from django.http import Http404
import abc 

class AgencyListAPIView(generics.ListCreateAPIView):
    """
    List all agencies, or create a new agency.
    """
    model = Agency
    queryset = Agency.objects.all()
    serializer_class = AgencySerializer
    #TODO permissions

class AgencyDetailAPIView(generics.RetrieveUpdateDestroyAPIView):
    """
    Retrieve, update or delete an agency instance.
    """
    model = Agency
    queryset = Agency.objects.all()
    serializer_class = AgencySerializer
    lookup_url_kwarg = 'agency_id'

class RouteListAPIView(generics.ListAPIView):
    """
    Retrieve a list of routes
    """
    model = Route
    serializer_class = RouteLightSerializer
    
    def get_queryset(self):
        agency_id = self.kwargs['agency_id']
        return Route.objects.filter(agency__id=agency_id)

class TypedRouteListAPIView(generics.ListAPIView):
    """
    Retrieve a list of routes
    """
    model = Route
    serializer_class = RouteLightSerializer
    
    def get_queryset(self):
        agency_id = self.kwargs['agency_id']
        rtype = self.kwargs['r_type']
        return Route.objects.filter(agency__id=agency_id,rtype=rtype)

class RouteDetailAPIView(generics.RetrieveUpdateDestroyAPIView):
    """
    Retrieve, update, or delete a route
    """
    model = Route
    queryset = Route.objects.all()
    serializer_class = RouteSerializer
    lookup_url_kwarg = 'route_id'

class TripListAPIView(generics.ListAPIView):
    """
    Retrieve all trips or create new one
    """
    model = Trip
    serializer_class = TripLightSerializer

    def get_queryset(self):
        route_id = self.kwargs['route_id']
        return Trip.objects.filter(route__id=route_id)

class DistinctTripListAPIView(APIView):
    """
    Retrieve distinct trips based on headname
    """
    pass
        
class TripDetailAPIView(APIView):
    """
    Retrieve, update, delete a trip
    """
    def __init__(self):
        self.model = Trip
        self.serializer = TripSerializer

    def get(self, request, trip_id):
        try:
            trip = self.model.objects.get(pk=trip_id)
            serialized = self.serializer(trip)
            return Response(serialized.data)
        except self.model.DoesNotExist:
            raise Http404

class RouteStopsListAPIView(generics.ListAPIView):
    """
    List all stops for a route
    Can provide arg to limit to stops with soonest stop times
    """
    model = Stop
    serializer_class = StopSerializer

    def get_queryset(self):
        route_id = self.kwargs['route_id']
        trip_ids = Trip.objects.filter(route__id = route_id).values_list("id")
        stop_ids =  StopTime.objects.filter(trip__id__in=trip_ids).values_list("stop__id")
        stops = Stop.objects.filter(id__in=stop_ids).distinct()
        return stops

class StopDetailAPIView(generics.RetrieveUpdateDestroyAPIView):
    """
    Retrieve, update or delete an stop time instance.
    """
    model = Stop
    queryset = Stop.objects.all()
    serializer_class = StopSerializer
    lookup_url_kwarg = 'stop_id'

class StopNextTimesAPIView(generics.ListAPIView):
    """
    Retrieve next stop time relative to passed in time.
    Time must be an int, which is the second 
    TODO have not figured out how to handle timezone well yet,
    (not always stored in stop), so just passing in time
    Can use lat,lon to get time zone and then get curr time, though.
    ---
    parameters:
        - name:stop_id
        description: The id of the stop
        required: true
        type: int
        - name: time
        description: The integer number of seconds since the start of the day to get stop times after
        required:true
        type:int
        - name: num
        description: The number of stop times to get
        required:false
        type:int

    """
    model = StopTime
    serializer_class = StopTimeSerializer

    def get_queryset(self):
        stop_id = self.kwargs['stop_id']
        time = self.kwargs['time']
        num=1
        if 'num' in self.kwargs:
            num = self.kwargs['num']
        stop = Stop.objects.get(id=stop_id)
        if stop.stoptime_set.aggregate(Max('arrival_time'))<time:
            time = 0
        timecompare = Seconds(int(time))
        stoptimes = stop.stoptime_set.filter(arrival_time__gt=timecompare).order_by('arrival_time')[0:num]
        stoptimes = stop.stoptime_set.filter(arrival_time__gt=timecompare).order_by('arrival_time')[0:num]
        return stoptimes

class StopTimesListAPIView(generics.ListAPIView):
    """
    Retrieve a list of stops times for a given stop
    """
    model = StopTime
    serializer_class = StopTimeSerializer
    
    def get_queryset(self):
        stop_id = self.kwargs['stop_id']
        return StopTime.objects.filter(stop__id=stop_id)

class StopTimeDetailAPIView(generics.RetrieveUpdateDestroyAPIView):
    """
    Retrieve, update or delete an stop time instance.
    """
    model = StopTime
    queryset = StopTime.objects.all()
    serializer_class = StopTimeSerializer
    lookup_url_kwarg = 'stop_time_id'

from django.contrib.gis.geos import Point
from django.contrib.gis.measure import D

class NearestStopsListAPIView(generics.ListAPIView):
    """
    Get the nearest stops. Should pass in lat, lot, and mile limit
    as URL query params. Not tested
    """
    model = Stop
    serializer_class = StopSerializer

    def get_queryset(self):
        lat = self.GET.get('lat')
        lon = self.GET.get('lon')
        limit = self.GET.get('limit')
        point = Point(lon,lat)
        stops = Stop.objects.filter(point__distance_lte=(point, D(mi=limit))
                                             ).distance(point).order_by('distance')
        return stops

class NearestAgencyAPIView(generics.ListAPIView):
    """
    Get the nearest agency of the nearest. Should pass in lat, lot, and mile limit
    as URL query params. Not tested.
    """
    model = Agency
    serializer_class = StopSerializer

    def get_queryset(self):
        lat = self.GET.get('lat')
        lon = self.GET.get('lon')
        limit = self.GET.get('limit')
        point = Point(lon,lat)
        stop = Stop.objects.filter(point__distance_lte=(point, D(mi=limit))
                                             ).distance(point).order_by('distance')[0]
        stoptime = stop.stop_time_set
        trip = stoptime.trip
        route = trip.route
        return route.agency

