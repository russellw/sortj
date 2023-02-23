import os
import re
import subprocess
import tempfile
import time


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
    print(" ".join(cmd))
    p = subprocess.Popen(
        cmd,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
    )
    stdout, stderr = p.communicate()
    stdout = str(stdout, "utf-8")
    stderr = str(stderr, "utf-8")
    if p.returncode or stderr:
        print(stderr, end="")
        raise Exception(str(p.returncode))
    if s in outputs:
        if stdout != open(s + "-out.java").read():
            raise Exception(s)
        return
    open(s + "-out.java", "w", newline="\n").write(s)


for s in inputs:
    do(s)
print("ok")
