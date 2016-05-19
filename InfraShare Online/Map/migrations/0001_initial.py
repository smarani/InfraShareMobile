# -*- coding: utf-8 -*-
# Generated by Django 1.9.2 on 2016-03-25 15:40
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='InfragramDocument',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date_taken', models.DateTimeField(verbose_name='date taken')),
                ('date_uploaded', models.DateTimeField(verbose_name='date uploaded')),
                ('location_description', models.CharField(max_length=500)),
                ('plant_description', models.CharField(max_length=200)),
                ('general_notes', models.CharField(max_length=500)),
                ('infragram_image', models.FileField(upload_to='documents/%Y/%m/%d')),
                ('original_image', models.FileField(upload_to='documents/%Y/%m/%d')),
            ],
        ),
    ]
