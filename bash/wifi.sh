#!/bin/bash

lagi='y'
while [ $lagi == 'y' ] || [ $lagi == 'Y' ];
do
clear
echo "MOHON, NALAKAN WIFINYA";
echo "";
echo "1.[peta hotspot]";
echo "2.[nyoto]";
echo "3.[smp ratmen]";
echo "4.[original]";
echo "0.[Lihat password]";
echo "";

read pil;
case $pil in
1)
busybox ifconfig wlan0 hw ether 00:CD:FE:D6:A3:50
busybox iplink show wlan0
;;
2)
busybox ifconfig wlan0 hw ether 6C:72:20:48:47:A5
busybox iplink show wlan0
;;
3)
busybox ifconfig p2p0 hw ether C4:17:FE:25:D9:7C
busybox iplink show p2p0
;;
4)
busybox ifconfig wlan0 hw ether E0:63:E5:D7:85:01
busybox iplink show wlan0
;;
0)
cat /data/misc/wifi/wpa_supplicant.conf
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
echo "Kembali ke Main wifi(y/n) : ";
read lagi;
done
