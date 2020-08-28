# usage: python data.py n1 n2 > ingest.data
#
# n1 is how many copies of each word group you want
# n2 is how many word groups to generate
# the total number of rows generated will be n1*n2
#
# then edit and use ingest.spec to ingest ingest.data

import random
import string
import sys
import time

# for word generation
VOWELS = "aeiouy"
CONSONANTS = "".join(set(string.ascii_lowercase) - set(VOWELS))

# "dup" is how many rows for each word group - for testing cardinalities, rollups, etc
# "count" is how many word groups to generate
# (total number of rows generated is dup*count)

dup = int(sys.argv[1])
count = int(sys.argv[2])

# word generator
def generate_word(length):
    word = ""
    for i in range(length):
        if i % 2 == 0:
            word += random.choice(CONSONANTS)
        else:
            word += random.choice(VOWELS)
    return word

# generate a timestamp in microseconds,
# a "word" group with random strings, 2 letters, 4 letters, and 8 letters (to get different cardinalities overall)
# and random latitude and longitude coordinates, to load as a coordinate point

for i in range(0,count):
    word2 = generate_word(2)
    word4 = generate_word(4)
    word8 = generate_word(8)
    for j in range(0,dup):
        print '{:.0f},{},{},{},{},{},{},{},{}'.format(1000000*time.time(),word2,word4,word8,random.randint(1,10),random.randint(1,1000),random.randint(1,10000),random.randint(-90,90),random.randint(-180,180))

