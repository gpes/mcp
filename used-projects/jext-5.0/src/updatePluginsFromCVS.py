#!/usr/bin/env python
import os

# Remove plugins you don't need from this list.
checkouts = [ "JavaPlugin", "Admin", "AntWork", "BeanMaker", "Bookmark", "ClearCase", "Code2HTML", "CodegeekPlugin", "DawnServer", "Decalco", "DickTracy", "Ed", "FunctionBrowser", "Gooey", "GTK", "IzPress", "JexMMS", "JextFE", "JFTPBrowser", "jHierarchy", "JIPrologPlugin", "JSBrowse", "JScriptPlugin", "JSwatPlugin", "JumpPlugin", "MemoryMonitor", "PlasticSkin", "PPC", "ProjectMaster", "Puyo", "PyBrowse", "QuickMake", "ServerMapping", "SkinLF", "SQLConsole", "TeXify", "Wapppaaa", "WheelMouse", "ZMachine" ]

try:
  os.mkdir("../extplugins")
except:
  pass
os.chdir("../extplugins")

for checkout in checkouts:
  print "*********** Updating", checkout, "plugin ***********"
  os.chdir(checkout)
  os.system("cvs -z3 -d:pserver:anonymous@cvs.jext.sourceforge.net:/cvsroot/jext-plugins update")
  os.chdir("..")
  print "\n"
