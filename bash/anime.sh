#!/bin/bash          

clear
echo "1. masukan namanya atau paste lalu enter"
echo "2. hapus unduhan lalu download ulang"
echo "3. buka terminal lalu enter, dan selesai"
echo "";
echo "Masukan/paste nama : "
read NAMA

cd /sdcard/UCDownloads
mv "$NAMA" /sdcard/UCDownloads/y
cp "$NAMA.dltemp" /sdcard/UCDownloads/y
rm "$NAMA.dltemp"
echo "";
echo "Silahkan hapus unduhan dan download ulang, kalau udah tekan enter";
read 
cd /sdcard/UCDownloads
rm "$NAMA";
rm "$NAMA.dltemp";
cd /sdcard/UCDownloads/y
mv "$NAMA" /sdcard/UCDownloads/
cp "$NAMA.dltemp" /sdcard/UCDownloads/
rm "$NAMA.dltemp"
echo "----sukses----";

