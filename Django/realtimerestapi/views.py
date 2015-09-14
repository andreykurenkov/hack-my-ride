from django.shortcuts import render
from rest_framework import generics
from rest_framework import generics
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from multigtfs.models import (
    Agency, Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip)
from transitrestapi.serializers import *
from django.http import HttpResponseNotFound
import abc 
import gtfs_realtime_pb2
import requests
import protobuf_json
from multigtfs.models.fields import Seconds
TOKEN = '10ee313e-23bb-4991-85f7-0bf6d6544bff'
BASE_URL = 'http://api.511.org/transit'

def get_trip_updates(agency_name):
    """
    :param agency_name: Name of agency. example: agency=vtaAgency
    :param stop_code: Numeric stop code for the stop to be monitored. 
                      When stop code is not provided, the API will return all available information for all stops. 
                      Depending on the amount of data, the response time for the API can be more than 5-7 seconds.
    """
    try:
        updates = gtfs_realtime_pb2.ParseFromString(data)
        #TODO parse data into JSON, use serializer
    except:
        return None

class TripsRealTimeUpdatesAPIView(APIView):
    """
    Get realtime data about a stop
    """
    def get(self, request, agency_name, format=None):
        request = BASE_URL+'/tripupdates?api_key=%s&agency=%s'%(TOKEN,agency_name)
        data = requests.get(request).content
        msg= gtfs_realtime_pb2.FeedMessage()
        msg.ParseFromString(data)
        return Response(protobuf_json.pb2json(msg))

class TripRealTimeUpdatesAPIView(APIView):
    """
    Get realtime data about a stop
    """
    def get(self, request, trip_id, format=None):
        trip = Trip.objects.all().get(pk=trip_id)
        agency = trip.route.agency
        request = BASE_URL+'/tripupdates?api_key=%s&agency=%s'%(TOKEN,agency.name)
        data = requests.get(request).content
        msg= gtfs_realtime_pb2.FeedMessage()
        msg.ParseFromString(data)
        json = protobuf_json.pb2json(msg)['entity']
        json_route = filter(lambda obj: obj['trip_update']['trip']['trip_id']==trip.trip_id,json)
        if len(json_route)==0:
            return HttpResopnseNotFound()
        return Response(json_route[0])

class StopRealTimeUpdatesAPIView(APIView):
    """
    Get realtime data about a stop, in POSIX time
    https://developers.google.com/transit/gtfs-realtime/reference#StopTimeEvent
    """

    def get(self,request,stop_time_id,format=None):
        stop_time = StopTime.objects.all().get(pk=stop_time_id)
        stop = stop_time.stop
        trip = stop_time.trip
        agency = trip.route.agency
        request = BASE_URL+'/tripupdates?api_key=%s&agency=%s'%(TOKEN,agency.name)
        data = requests.get(request).content
        msg= gtfs_realtime_pb2.FeedMessage()
        msg.ParseFromString(data)
        json = protobuf_json.pb2json(msg)['entity']
        for obs in json:
            for stop_time_update in obs['trip_update']['stop_time_update']:
                if stop_time_update['stop_id']==stop.code and 'departure' in stop_time_update:
                    return Response({'seconds':long(stop_time_update['departure']['time'])})
        return HttpResponseNotFound()

class StopRealTimeAPIView(APIView):
    """
    Get realtime data about a stop
    """
    def get(self, request, stop_id, format=None):
        stop = Stop.objects.get(id=stop_id)
        code = str(stop.code)
        agency_name = stop.stoptime_set.all()[0].trip.route.agency.name
        #TODO get agency_name from stop_id
        request = BASE_URL+'/StopMonitoring?api_key=%s&format=json&agency=%s&stopCode=%s'%(TOKEN,agency_name,code)
        response = requests.get(request)
        response.encoding = "utf-8-sig"
        return Response(response.json())
