import gtfs_realtime_pb2
import requests
import protobuf_json
from multigtfs.models import StopTime,Trip,Stop
from django.http import Http404
import traceback
TOKEN = '10ee313e-23bb-4991-85f7-0bf6d6544bff'
BASE_URL = 'http://api.511.org/transit'

def getRealTimeTripUpdates(agency_name):
    try:
        request = BASE_URL+'/tripupdates?api_key=%s&agency=%s'%(TOKEN,agency_name)
        data = requests.get(request).content
        msg= gtfs_realtime_pb2.FeedMessage()
        msg.ParseFromString(data)
        return protobuf_json.pb2json(msg)['entity']
    except:
        raise Http404(traceback.format_exc())

def getRealTimeTripUpdatesForTrip(trip_id):
    try:
        trip = Trip.objects.all().get(pk=trip_id)
        agency = trip.route.agency
        json = getRealTimeTripUpdates(agency.name)
        json_route = filter(lambda obj: obj['trip_update']['trip']['trip_id']==trip.trip_id,json)
        if len(json_route)==0:
            return None
        return json_route[0]
    except:
        raise Http404(traceback.format_exc())

def getRealTimeTripUpdatesFromStopTime(stop_time_id):
    try:
        stop_time = StopTime.objects.all().get(pk=stop_time_id)
        stop = stop_time.stop
        trip = stop_time.trip
        agency = trip.route.agency
        json = getRealTimeForTrip(trip.pk)
        for stop_time_update in json_trip['trip_update']['stop_time_update']:
            if stop_time_update['stop_id']==stop.code and 'departure' in stop_time_update:
                return stop_time_update
        return None
    except:
        raise Http404(traceback.format_exc())

def getDepartureTimeForStopTime(stop_time_id):
    json = getRealTimeTripUpdatesFromStopTime(stop_time_id)
    if json==None:
        return None
    return long(json['departure']['time'])

def getStopMonitoringInfo(stop_id):
    stop = Stop.objects.get(id=stop_id)
    code = str(stop.code)
    agency_name = stop.stoptime_set.all()[0].trip.route.agency.name
    request = BASE_URL+'/StopMonitoring?api_key=%s&format=json&agency=%s&stopCode=%s'%(TOKEN,agency_name,code)
    response = requests.get(request)
    response.encoding = "utf-8-sig"
    return respones.json()
