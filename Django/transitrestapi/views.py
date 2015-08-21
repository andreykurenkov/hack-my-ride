from django.shortcuts import render
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from multigtfs.models import (
    Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip)
from transitrestapi.serializers import *
# Create your views here.

class StopListAPIView(APIView):
    """
    List all snippets, or create a new snippet.
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
    Retrieve, update or delete a snippet instance.
    """
    def get_object(self, pk):
        try:
            return Stop.objects.get(pk=pk)
        except Stop.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        stop = self.get_object(pk)
        serializer = StopSerializer(stop)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        stop = self.get_object(pk)
        serializer = StopSerializer(stop, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        stop = self.get_object(pk)
        stop.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

class RouteListAPIView(APIView):
    """
    List all snippets, or create a new route.
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
