from django.shortcuts import render
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from multigtfs.models import (
    Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip)
from transitrestapi.serializers import *

# Create your views here.
class ServiceListAPIView(APIView):
    """
    List all services, or create a new service.
    """
    def get(self, request, format=None):
        services = Service.objects.all()
        serializer = ServiceSerializer(services, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = ServiceSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class ServiceDetailAPIView(APIView):
    """
    Retrieve, update or delete a service instance.
    """
    def get_object(self, pk):
        try:
            return Service.objects.get(pk=pk)
        except Service.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        service = self.get_object(pk)
        serializer = ServiceSerializer(service)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        service = self.get_object(pk)
        serializer = ServiceSerializer(service, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        service = self.get_object(pk)
        service.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

# Create your views here.
class TripListAPIView(APIView):
    """
    List all trips, or create a new trip.
    """
    def get(self, request, format=None):
        trips = Trip.objects.all()
        serializer = TripSerializer(trips, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = TripSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class TripDetailAPIView(APIView):
    """
    Retrieve, update or delete a trip instance.
    """
    def get_object(self, pk):
        try:
            return Trip.objects.get(pk=pk)
        except Trip.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        trip = self.get_object(pk)
        serializer = TripSerializer(trip)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        trip = self.get_object(pk)
        serializer = TripSerializer(trip, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        trip = self.get_object(pk)
        trip.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class StopListAPIView(APIView):
    """
    List all stop, or create a new stop.
    """
    def get(self, request, format=None):
        stops = Stop.objects.all()
        serializer = StopSerializer(stops, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = StopSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class StopDetailAPIView(APIView):
    """
    Retrieve, update or delete a stop instance using stop_id.
    """
    def get_object(self, stop_id):
        try:
            return Stop.objects.get(stop_id=stop_id)
        except Stop.DoesNotExist:
            raise Http404

    def get(self, request, stop_id, format=None):
        stop = self.get_object(stop_id)
        serializer = StopSerializer(stop)
        return Response(serializer.data)

    def put(self, request, stop_id, format=None):
        stop = self.get_object(stop_id)
        serializer = StopSerializer(stop, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, stop_id, format=None):
        stop = self.get_object(stop_id)
        stop.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

class StopTimesListAPIView(APIView):
    """
    Retrieve stop times for a stop based on stop_id.
    """
    def get_object(self, stop_id):
        try:
            return Stop.objects.get(stop_id=stop_id).stoptime_set.all()
        except Stop.DoesNotExist:
            raise Http404

    def get(self, request, stop_id, format=None):
        stop_time = self.get_object(stop_id)
        serializer = StopTimeSerializer(stop_time, many=True)
        return Response(serializer.data)

class RouteListAPIView(APIView):
    """
    List all routes, or create a new route.
    """
    def get(self, request, format=None):
        routes = Route.objects.all()
        serializer = RouteSerializer(routes, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = RouteSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class RouteDetailAPIView(APIView):
    """
    Retrieve, update or delete a route instance.
    """
    def get_object(self, pk):
        try:
            return Route.objects.get(pk=pk)
        except Route.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        route = self.get_object(pk)
        serializer = RouteSerializer(route)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        route = self.get_object(pk)
        serializer = RouteSerializer(stop, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        route = self.get_object(pk)
        route.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
