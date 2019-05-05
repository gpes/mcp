#!/usr/bin/env python
import os

# Remove plugins you don't need from this list.
checkouts = [ "Admin", "AntWork", "BeanMaker", "Bookmark", "ClearCase", "Code2HTML", "CodegeekPlugin", "DawnServer", "Decalco", "DickTracy", "Ed", "FunctionBrowser", "Gooey", "GTK", "IzPress", "JexMMS", "JextFE", "JFTPBrowser", "jHierarchy", "JIPrologPlugin", "JSBrowse", "JScriptPlugin", "JSwatPlugin", "JumpPlugin", "MemoryMonitor", "PlasticSkin", "PPC", "ProjectMaster", "Puyo", "PyBrowse", "QuickMake", "ServerMapping", "SkinLF", "SQLConsole", "TeXify", "Wapppaaa", "WheelMouse", "ZMachine" ]

try:
  os.mkdir("../extplugins")
except:
  pass
os.chdir("../extplugins")

print "** No password is required, just press Enter **"
os.system("cvs -d:pserver:anonymous@cvs.sourceforge.net:/cvsroot/jext-plugins login")

for checkout in checkouts:
  print "*********** Checking out", checkout, "plugin ***********"
  os.system("cvs -z3 -d:pserver:anonymous@cvs.jext.sourceforge.net:/cvsroot/jext-plugins checkout " + checkout)
  print "\n"
