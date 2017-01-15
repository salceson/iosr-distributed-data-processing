#!/usr/bin/env python3
import sys

argv = sys.argv[1:]

for filename in argv:
    print('Processing file %s' % filename)
    with open(filename, 'r') as rf:
        with open('out/%s' % filename, 'w') as wf:
            first = True
            for line in rf:
                if first:
                    first = False
                    continue
                line = line.strip()
                if line.endswith(','):
                    line = line[:-1]
                print(line, file=wf)
