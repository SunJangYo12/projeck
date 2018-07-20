#!/bin/bash

cd /sdcard/bash
lagi='y'
while [ $lagi == 'y' ] || [ $lagi == 'Y' ];
do
clear
echo "_________versi  0.1_________";
echo "";
echo "Pilihan : "
echo "";
echo "1.[root/exit]";
echo "2.[wifi]";
echo "3.[anime download]";
echo "";

read pil;
case $pil in
1)
su
;;
2)
sh /sdcard/bash/wifi.sh
;;
3)
sh /sdcard/bash/anime.sh
;;
3)
;;
1337)
exit 0
;;
*)
echo "[!]  Maaf, nomor salah"
exit 1
;;
esac
echo "";
echo "";
echo "Kembali ke Main Menu(y/n) : ";
read lagi;
done



