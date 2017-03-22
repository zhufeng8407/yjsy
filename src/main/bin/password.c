#include <conio.h>
#include <stdio.h>
char pw[40];
int i,ch;
FILE *f;
void main() {
    cprintf("\r\nPassword:");
    i=0;pw[i]=0;
    while (1) {
        ch=getch();
        if (ch==13 || i>=39) break;
        switch (ch) {
        case 27:
            cprintf("\rPassword: %40s"," ");
            cprintf("\rPassword: ");
            i=0;pw[i]=0;
            break;
        case 8:
            if (i>0) {
                i--;
                pw[i]=0;
                cprintf("\b \b");
            }
            break;
        default:
            pw[i]=ch;
            i++;
            pw[i]=0;
            cprintf("*");
            break;
        }
    }
    cprintf("\r\n");
    f=fopen("password.txt","w");
    fprintf(f,"%s\n",pw);
    fclose(f);
}