filename = "./features/evosuite.arff"
import os


def toCsv(content):
    data = False
    header = ""
    newContent = []
    for line in content:
        if not data:
            if "@ATTRIBUTE" in line:
                attri = line.split()
                columnName = attri[attri.index("@ATTRIBUTE") + 1]
                header = header + columnName + ","
            elif "@DATA" in line:
                data = True
                header = header[:-1]
                header += '\n'
                newContent.append(header)
        else:
            newContent.append(line)
    return newContent


# Main loop for reading and writing files
if __name__ == '__main__':
    with open(filename, "r") as inFile:
        content = inFile.readlines()
        name, ext = os.path.splitext(inFile.name)
        new = toCsv(content)
        with open(name + ".csv", "w") as outFile:
            outFile.writelines(new)
