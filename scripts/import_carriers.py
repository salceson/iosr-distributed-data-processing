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

sql_template = 'INSERT INTO public.carriers ("year", "week", "carrier", "from", "to", "arrival_sum", "departure_sum",' \
               ' "num") VALUES (%s, %s, %s, %s, %s, %s, %s, %s);'

for filename in argv:
    print('Processing file %s' % filename)
    with open(filename, 'r') as f:
        reader = csv.reader(f)
        for row in reader:
            year = int(row[0])
            week = int(row[1])
            carrier = row[2]
            from_airport = row[3]
            to_airport = row[4]
            departure_delay = float(row[5])
            arrival_delay = float(row[6])
            num = int(row[7])
            cursor.execute(sql_template,
                           (year, week, carrier, from_airport, to_airport, arrival_delay, departure_delay, num))

conn.commit()
cursor.close()
conn.close()
