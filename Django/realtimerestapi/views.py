from django.shortcuts import render
from rest_framework import generics
from rest_framework import generics
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from multigtfs.models import Trip,Stop
from transitrestapi.serializers import *
from django.http import HttpResponseNotFound
from django.http import Http404
import realtime
TOKEN = '10ee313e-23bb-4991-85f7-0bf6d6544bff'
BASE_URL = 'http://api.511.org/transit'
class TripsRealTimeUpdatesAPIView(APIView):
    """
    Get realtime data about trips
    """
    def get(self, request, agency_name, format=None):
        return Response(realtime.getRealTimeTripUpdates(agency_name))

class StopsWithRealTimeDataAPIView(APIView):
    """
    Get realtime data about stops
    """
    def get(self, request, agency_name, format=None):
        return Response(realtime.getStopsWithRealTimeData(agency_name))

class TripRealTimeUpdatesAPIView(APIView):
    """
    Get realtime data about a stop
    """
    def get(self, request, trip_id, format=None):
        return Response(realtime.getRealTimeTripUpdatesForTrip(trip_id))

class StopRealTimeUpdatesAPIView(APIView):
    """
    Get realtime data about a stop, in POSIX time
    https://developers.google.com/transit/gtfs-realtime/reference#StopTimeEvent
    """

    def get(self,request,stop_time_id,format=None):
        value = realtime.getDepartureTimeForStopTime(stop_time_id)
        if value==None:
            raise Http404('Cannot get time')
        return Response({'seconds':value})


class StopRealTimeAPIView(APIView):
    """
    Get realtime data about a stop
    """
    def get(self, request, stop_id, format=None):
        return Response(realtime.getStopMonitoringInfo(stop_id))
