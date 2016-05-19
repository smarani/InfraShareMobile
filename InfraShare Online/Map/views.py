from django.shortcuts import render

# -*- coding: utf-8 -*-
from django.shortcuts import render_to_response
from django.template import RequestContext
from django.http import HttpResponseRedirect
from django.core.urlresolvers import reverse
from django.http import HttpResponse
import exifread
from Map.models import InfragramDocument
from Map.forms import DocumentForm
import datetime
from django.views.decorators.csrf import csrf_exempt
import sys
import string


@csrf_exempt
def upload(request):
    # Handle file upload
    if request.method == 'POST':
        correct_date = string.replace(request.POST['date_taken'], ':', '-', 2)
        if correct_date == 'null':
        	correct_date = datetime.datetime.now()

        newdoc = InfragramDocument(
        	infragram_image = request.FILES['infragram_image'], 
        	original_image = request.FILES['original_image'],
        	location_description = request.POST['location_description'],
        	plant_description = request.POST['plant_description'],
        	general_notes = request.POST['general_notes'],
        	date_taken = correct_date,
        	date_uploaded = datetime.datetime.now() - datetime.timedelta(hours=4), 
        	latitude = request.POST['latitude'],
        	longitude = request.POST['longitude'], 
        	ndvi_score = request.POST['ndvi_score']
        	)
        print newdoc.plant_description
        sys.stdout.flush()
        newdoc.save()
        newdate = newdoc.date_taken - datetime.timedelta(hours=4)
        newdoc.date_taken = newdate
        newdoc.save()
           

            # Redirect to the document list after POST
        return HttpResponseRedirect(reverse('index'))
    else:
        form = DocumentForm() # A empty, unbound form

    # Load documents for the list page
    documents = InfragramDocument.objects.all()

    return render_to_response('Map/list.html', {'documents' : documents, 'form': form}, context_instance=RequestContext(request))

def index(request):
	#return HttpResponse("Hello, world. You're at the polls index.")
	documents = InfragramDocument.objects.all()
	return render_to_response('Map/photogps.html', {'documents' : documents}, context_instance=RequestContext(request))

    