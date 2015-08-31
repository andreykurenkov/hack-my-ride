from rest_framework import serializers
from multigtfs.models import (
    Agency, Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip)

class AgencySerializer(serializers.ModelSerializer):
    class Meta:
        model = Agency

class BlockSerializer(serializers.ModelSerializer):
    class Meta:
        model = Block

class FareSerializer(serializers.ModelSerializer):
    class Meta:
        model = Fare

class FareRuleSerializer(serializers.ModelSerializer):
    class Meta:
        model = FareRule

class FeedSerializer(serializers.ModelSerializer):
    class Meta:
        model = Feed

class FrequencySerializer(serializers.ModelSerializer):
    class Meta:
        model = Frequency

class ServiceSerializer(serializers.ModelSerializer):
    class Meta:
        model = Service

class ServiceDateSerializer(serializers.ModelSerializer):
    class Meta:
        model = ServiceDate

class StopSerializer(serializers.ModelSerializer):
    class Meta:
        model = Stop

class StopTimeSerializer(serializers.ModelSerializer):
    class Meta:
        model = StopTime

class RouteSerializer(serializers.ModelSerializer):
    agency = AgencySerializer()
    class Meta:
        model = Route

class RouteLightSerializer(serializers.Serializer):
    route_id = serializers.CharField(max_length=255)
    agency = AgencySerializer()
    short_name = serializers.CharField(max_length=63)
    long_name = serializers.CharField()
    rtype = serializers.IntegerField()
    url = serializers.URLField()
    id = serializers.IntegerField()

class TripSerializer(serializers.ModelSerializer):
    class Meta:
        model = Trip

class TripLightSerializer(serializers.Serializer):
    id = serializers.IntegerField()
    trip_id = serializers.CharField(max_length=63)
    short_name = serializers.CharField(max_length=63)
    direction = serializers.CharField(max_length=10)
    headsign = serializers.CharField(max_length=63)
    wheelchair_accessible = serializers.CharField(max_length=10)
    bikes_allowed = serializers.CharField(max_length=10)
    route = RouteLightSerializer()
