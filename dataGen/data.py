# usage: python data.py n1 n2
# where n1 is how many copies of each dimension group you want (for rollup testing)
# and n2 is how many word groups to generate
# total number of rows generated will be n1*n2

import random
import string
import sys
import time

# for word generation
VOWELS = "aeiouy"
CONSONANTS = "".join(set(string.ascii_lowercase) - set(VOWELS))

# "dup" is how many rows for each dimension group - for testing cardinalities, rollups, etc
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
# a "word" group with random strings, 2 letters, 4 letters, and 8 letters (to get different cardinalities overall),
# a pseudo-random lat and lon to create a coordinate point, 
# (those are the dimensions),
# and 3 pseudo-random numbers of different sizes
# (the metrics)

for i in range(0,count):
    word2 = generate_word(2)
    word4 = generate_word(4)
    word8 = generate_word(8)
    lat = random.randint(-90,90)
    lon = random.randint(-180,180)
    int1 = random.randint(1,10)
    int3 = random.randint(1,1000)
    int5 = random.randint(1,100000)
    for j in range(0,dup):
	if i % 10 == 0:
		skew_lo = random.randint(1,9999999)
		skew_hi = random.randint(1,100)
	else:
		skew_lo = random.randint(1,100)
		skew_hi = random.randint(1,9999999)
	if i % 2 == 0:
		lo_hi = random.randint(1,100)
	else:
		lo_hi = random.randint(1,9999999)

        print '{:.0f},{},{},{},{},{},{},{},{},{},{},{}'.format(1000000*time.time(),word2,word4,word8,lat,lon,int1,int3,int5,skew_lo,skew_hi,lo_hi)

