from django.shortcuts import render
from rest_framework import generics
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from multigtfs.models import (
    Agency, Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip)
from transitrestapi.serializers import *
from django.http import Http404
import abc 

class AgencyListAPIView(generics.ListCreateAPIView):
    """
    List all agencies, or create a new agency.
    """
    queryset = Agency.objects.all()
    serializer_class = AgencySerializer
    #TODO permissions

class AgencyDetailAPIView(generics.RetrieveUpdateDestroyAPIView):
    """
    Retrieve, update or delete a agency instance.
    """
    queryset = Agency.objects.all()
    serializer_class = AgencySerializer
    lookup_field = 'agency_id'

class BasicListAPIView():
    """
    Base class for basic REST list views (list or add new)
    Note that subclass extends this and APIView, so that it
    is automatically in api-doc.
    """
    def __init__(self,model_class,serializer_class):
        self.model = model_class
        self.serializer = serializer_class
    
    def get(self, request, agency_id, format=None):
        objects = self.model.objects.get(agency__agency_id=agency_id)
        serialized = self.serializer(objects, many=True)
        return Response(serialized.data)

    def post(self, request, agency_id, format=None):
        #TODO verify agency_id in JSON?
        serialized = self.serializer(data=request.data)
        if serialized.is_valid():
            serialized.save()
            return Response(serialized.data, status=status.HTTP_201_CREATED)
        return Response(serialized.errors, status=status.HTTP_400_BAD_REQUEST)

class BasicDetailAPIView():
    """
    Base class for basic REST views.
    Note that subclass extends this and APIView, so that it
    is automatically in api-doc.
    """
    def __init__(self,model_class,serializer_class):
        self.model = model_class
        self.serializer = serializer_class

    @abc.abstractmethod
    def get_object(self, agency_id, res_id):
        raise Http404

    def get(self, request, agency_id, res_id, format=None):
        obj = self.get_object(agency_id, res_id)
        serialized = self.serializer(obj)
        return Response(serialized.data)

    def put(self, request, agency_id, res_id, format=None):
        obj = self.get_object(agency_id, res_id)
        serialized = self.serializerr(obj, data=request.data)
        if serialized.is_valid():
            serialized.save()
            return Response(serialized.data)
        return Response(serialized.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, res_id, format=None):
        route = self.get_object(res_id)
        route.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

class ServiceListAPIView(BasicListAPIView, APIView):
    """
    List all services, or create a new service.
    """
    def __init__(self):
        BasicListAPIView.__init__(self,Service,ServiceSerializer)

class ServiceDetailAPIView(BasicDetailAPIView, APIView):
    """
    Retrieve, update or delete a service instance.
    """
    def __init__(self):
        BasicDetailAPIView.__init__(self,Service,ServiceSerializer)
    
    def get_object(self, agency_id, res_id):
        try:
            return self.model.objects.get(agency__agency_id=agency_id, service_id=res_id)
        except self.model.DoesNotExist:
            raise Http404

class TripListAPIView(BasicListAPIView, APIView):
    """
    List all trips, or create a new trip.
    """
    def __init__(self):
        BasicListAPIView.__init__(self,Trip,TripSerializer)

class TripDetailAPIView(BasicDetailAPIView,APIView):
    """
    Retrieve, create, update or delete a trip instance.
    """
    def __init__(self):
        BasicDetailAPIView.__init__(self,Trip,TripSerializer)
    
    def get_object(self, agency_id, res_id):
        try:
            return self.model.objects.get(agency__agency_id=agency_id, trip_id=res_id)
        except self.model.DoesNotExist:
            raise Http404

class StopTimesListAPIView(BasicListAPIView,APIView):
    """
    List all stops time for a certain agency or create a new one.
    """
    def __init__(self):
        BasicListAPIView.__init__(self,Stop,StopSerializer)

class StopTimeDetailAPIView(APIView):
    """
    Retrieve, create, update or delete a stop instance.
    """
    def __init__(self):
        BasicDetailAPIView.__init__(self,StopTime,StopTimeSerializer)
    
    def get_object(self, agency_id, res_id):
        try:
            return self.model.objects.get(agency__agency_id=agency_id, pk=res_id)
        except self.model.DoesNotExist:
            raise Http404


class StopListAPIView(BasicListAPIView, APIView):
    """
    List all stops for a given agency or create a new one.
    """
    def __init__(self):
        BasicListAPIView.__init__(self,Stop,StopSerializer)

class StopDetailAPIView(BasicDetailAPIView,APIView):
    """
    Retrieve, create, update or delete a stop instance.
    """
    def __init__(self):
        BasicDetailAPIView.__init__(self,Stop,StopSerializer)
    
    def get_object(self, agency_id, res_id):
        try:
            return self.model.objects.get(agency__agency_id=agency_id, stop_id=red_is)
        except self.model.DoesNotExist:
            raise Http404

class StopDetailTimesAPIView(APIView):
    """
    Retrieve stop times for a stop based on stop_id.
    """
    def get_object(self, agency_id, stop_id):
        try:
            return Stop.objects.get(agency__agency_id=agency_id,stop_id=stop_id).stoptime_set.all()
        except Stop.DoesNotExist:
            raise Http404

    def get(self, request, stop_id, format=None):
        stop_time = self.get_object(stop_id)
        serializer = StopTimeSerializer(stop_time, many=True)
        return Response(serializer.data)

class RouteListAPIView(BasicListAPIView,APIView):
    """
    List all routes, without the geo route data.
    """
    def __init__(self):
        BasicListAPIView.__init__(self,Route,RouteLightSerializer)
        self.post = None

class RouteDetailAPIView(BasicDetailAPIView,APIView):
    """
    Retrieve, update or delete a route instance.
    """
    def __init__(self):
        BasicDetailAPIView.__init__(self,Route,RouteSerializer)
    
    def get_object(self, agency_id, res_id):
        try:
            return self.model.objects.get(agency__agency_id=agency_id, route_id=red_id)
        except self.model.DoesNotExist:
            raise Http404

