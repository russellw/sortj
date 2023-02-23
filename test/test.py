import argparse
import os
import re
import subprocess
import tempfile
import time


parser = argparse.ArgumentParser(description="Run test cases")
parser.add_argument("-v", "--verbose", action="count", help="increase output verbosity")
parser.add_argument("files", nargs="*")
args = parser.parse_args()

verbose = 0
if args.verbose:
    verbose = args.verbose

here = os.path.dirname(os.path.realpath(__file__))
projectDir = os.path.join(here, "..")

jars = []
for root, dirs, files in os.walk(os.path.join(projectDir, "target")):
    for file in files:
        if file.endswith("-jar-with-dependencies.jar"):
            file = os.path.join(root, file)
            jars.append(file)
if len(jars) != 1:
    raise Exception("Expected one jar, found " + str(jars))
jar = jars[0]

inputs = set()
outputs = set()
for root, dirs, files in os.walk(here):
    for file in files:
        file = os.path.join(root, file)
        if file.endswith("-in.java"):
            inputs.add(file[:-8])
        elif file.endswith("-out.java"):
            outputs.add(file[:-9])
inputs = sorted(inputs)
outputs = sorted(outputs)

for s in outputs:
    if s not in inputs:
        raise Exception(s)


def do(s):
    cmd = "java", "-ea", "--enable-preview", "-jar", jar, s + "-in.java"
    print(cmd)


for s in inputs:
    do(s)
