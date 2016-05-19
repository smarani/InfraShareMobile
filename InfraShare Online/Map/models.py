from __future__ import unicode_literals

from django.db import models

# Create your models here.
class InfragramDocument(models.Model):
    date_taken = models.DateTimeField('date taken')
    date_uploaded = models.DateTimeField('date uploaded')
    location_description = models.CharField(max_length=500)
    plant_description = models.CharField(max_length=200)
    general_notes = models.CharField(max_length=500)
    latitude = models.DecimalField(max_digits=20, decimal_places=10)
    longitude = models.DecimalField(max_digits=20, decimal_places=10)
    infragram_image = models.FileField(upload_to='documents/%Y/%m/%d')
    original_image = models.FileField(upload_to='documents/%Y/%m/%d')
    ndvi_score = models.DecimalField(max_digits=20, decimal_places = 10)


