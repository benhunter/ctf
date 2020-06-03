

def read_audit(before, now, user):
    auparam = " -sc EXECVE"
    cmd = "ausearch -ts " + before.strftime('%H:%M:%S') + " -te " + now.strftime('%H:%M:%S') + " -ua " + user + auparam
    p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE)
    res = p.stdout.read().decode()
    return res