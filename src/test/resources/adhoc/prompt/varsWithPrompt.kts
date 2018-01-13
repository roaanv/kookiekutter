v["thingy"] = Prompt("thingyDefault")
v["theVar"] = Prompt(v["thingy"], "var theVar")
v["noDefault"] = Prompt()