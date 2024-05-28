import os

base_path = "./src/"

all_java_file_paths = list()

for (path, dir, files) in os.walk(base_path):
    for filename in files:
        ext = os.path.splitext(filename)[-1]
        if ext == '.java':
            all_java_file_paths.append("{}/{}".format(path, filename))

errors = list()

for java_file_path in all_java_file_paths:
    java_file = open(java_file_path, "r")
    
    for i in range(1):
        java_file.readline()
    
    second_line = java_file.readline().strip()
    
    if not second_line:
        errors.append(java_file_path)

    if not second_line.startswith("* Copyright"):
        errors.append(java_file_path)

    java_file.close()

if len(errors) != 0:
    print("please add lisence in top of java class")
    for error in errors:
        print(error)
    exit(1)

print("job success")
exit(0)