import datetime
from django.contrib.gis.db import models
from django.db import models
from django.utils import timezone

class TransitSystem(models.Model):
    name = models.CharField(max_length=200)

BUS = 'BUS'
TRAIN = 'TRAIN'
METRO = 'METRO'
LIGHT_TRAIN = 'LIGHT_TRAIN'
BOAT = 'BOAT'
OTHER = 'OTHER'
class Vehicle(models.Model):
    TYPES = (
        (BUS, 'Bus'),
        (TRAIN, 'Train'),
        (METRO, 'Metro'),
        (LIGHT_TRAIN, 'Light Train'),
        (BOAT, 'Boat'),
        (OTHER, 'Other'),
    )
    name = models.CharField(max_length=200)
    capacity = models.IntegerField(default=0)
    vid = models.IntegerField(default=0)
    speed = models.IntegerField(default=0)
    vehicletype = models.CharField(max_length=20, choices=TYPES)

class Route(models.Model):
    transitsystem = models.ForeignKey(TransitSystem)
    vehicle = models.ForeignKey(Vehicle)

class Address(models.Model):
    num = models.IntegerField()
    street = models.CharField(max_length=100)
    city = models.CharField(max_length=100)
    state = models.CharField(max_length=2)
    zipcode = models.ForeignKey(Zipcode)
    objects = models.GeoManager()

class Stop(models.Model):
    route = models.ForeignField(Route)
    predicted_arrival_time = models.DateTimeField('Arrival Time')
    location = models.GeometryField()
    objects = models.GeoManager()
    address = models.OneToOneField(Address)
    arrival_variance = models.DurationField(default=0)

DAYS_OF_WEEK = (
('1', 'Monday'),
('2', 'Tuesday'),
('3', 'Wednesday'),
('4', 'Thursday'),
('5', 'Friday'),
('6', 'Saturday'),
('7', 'Sunday'),
)

class StopTime(models.Model):
    stop = models.ForeignField(Stop)
    day = models.CharField(max_length=1, choices=DAYS_OF_WEEK)
    time = models.TimeField('Stop Time')

class IntervalStopTime(StopTime):
    day = models.CharField(max_length=1, choices=DAYS_OF_WEEK)
    time = models.TimeField('Stop Time')
    interval = models.DurationField(default=0)

class DatestopTime(StopTime):
    date = models.DateField('Sop Date')


