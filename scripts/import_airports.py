#!/usr/bin/env python3
import csv
import os
import psycopg2
import sys

argv = sys.argv[1:]

user = os.environ['DB_USER']
password = os.environ['DB_PASSWORD']

conn = psycopg2.connect(dbname='iosr', user=user, password=password)

cursor = conn.cursor()

sql_template = 'INSERT INTO public.airports ("year", "week", "airport", "num") VALUES (%s, %s, %s, %s);'

for filename in argv:
    print('Processing file %s' % filename)
    with open(filename, 'r') as f:
        reader = csv.reader(f)
        for row in reader:
            year = int(row[0])
            week = int(row[1])
            airport = row[2]
            num = int(row[3])
            cursor.execute(sql_template, (year, week, airport, num))

conn.commit()
cursor.close()
conn.close()
