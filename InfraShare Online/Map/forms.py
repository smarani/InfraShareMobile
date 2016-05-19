# -*- coding: utf-8 -*-
from django import forms

class DocumentForm(forms.Form):
    infragram_image = forms.FileField(
        label='Select a file',
    )
    original_image = forms.FileField(
        label='Select a file',
    )
    date_taken = forms.DateField(
    	label='What date was this taken?'
    )
    location_description = forms.CharField(
    	label='Where was this taken?', 
    	max_length=500
    )
    plant_description = forms.CharField(
    	label='What type of plant is it?', 
    	max_length=200
    )
    general_notes = forms.CharField(
    	label='Any general notes?', 
    	max_length=500
    )
    latitude = forms.DecimalField(
    	label= "Latitude?")
    longitude = forms.DecimalField(
    	label="Longitude?")
    ndvi_score = forms.DecimalField(
    	label="NDVI Score?")

