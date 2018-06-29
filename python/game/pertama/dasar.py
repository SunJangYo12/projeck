import pygame
import math
from pygame.locals import *
from random import *

pygame.init()

width, height = 640, 480
screen      = pygame.display.set_mode((width, height))
running     = True
playerpos   = [100, 100]
skor        = 0
panahs      = []
musuh_timer = 100
musuhs      = [[width, 100]]
tombol      = {
                "atas" :False,
                "bawah":False,
                "kiri" :False,
                "kanan":False,
}

player    = pygame.image.load("resources/images/dude.png")
rumput    = pygame.image.load("resources/images/grass.png")
rumah     = pygame.image.load("resources/images/castle.png")
panah     = pygame.image.load("resources/images/bullet.png")
musuh_img = pygame.image.load("resources/images/badguy.png")

while(running):
   screen.fill(0)

   for x in range(int(width/rumput.get_width()+1)):
      for y in range(int(height/rumput.get_height()+1)):
         screen.blit(rumput, (x*100, y*100))

   for peluru in panahs:
      panah_index = 0
      # untuk menentukan kecepatan panah dari titik awal ke akhir
      velx = math.cos(peluru[0])*10
      vely = math.sin(peluru[0])*10
      peluru[1] += velx
      peluru[2] += vely
      # untuk mengecek panah sudah mencapai batas layar atau belum
      if peluru[1] < -64 or peluru[1] > width or peluru[2] < -64 or peluru[2] > height:
         panahs.pop(panah_index) # hapus menggunakan pop

      panah_index += 1
      # gambar hasil
      for projectile in panahs:
         new_panah = pygame.transform.rotate(panah, 360-projectile[0]*57.29)
         screen.blit(new_panah, (projectile[1], projectile[2]))
   screen.blit(rumah, (0, 30))
   screen.blit(rumah, (0, 135))
   screen.blit(rumah, (0, 240))
   screen.blit(rumah, (0, 345))

  # menghitung sudut
   posisi_mouse = pygame.mouse.get_pos()
   sudut = math.atan2(posisi_mouse[1] - (playerpos[1]+32),
                      posisi_mouse[0] - (playerpos[0]+26))
  # memproses sudut
   player_rotasi = pygame.transform.rotate(player, 360 - sudut * 57.29)
   new_playerpos = (playerpos[0] - player_rotasi.get_rect().width/2,
                    playerpos[1] - player_rotasi.get_rect().height/2)
   screen.blit(player_rotasi, new_playerpos)

   musuh_timer -= 1
   if musuh_timer == 0:
      musuhs.append([width, randint(50, height-32)]) # buat musuh baru
      musuh_timer = randint(1, 100) # reset musuh ke random time
   index = 0
   for musuh in musuhs:
      musuh[0] -= 5 # musuh bergerak berkecepatan 5 px ke kiri
      # hapus musuh saat mencapai batas layar ke kiri
      if musuh[0] < -64:
         musuhs.pop(index)
   for musuh in musuhs:
      screen.blit(musuh_img, musuh)

   # colision markas
   musuh_rect     = pygame.Rect(musuh_img.get_rect())
   musuh_rect.top = musuh[1]
   musuh_rect.left= musuh[0]
   if musuh_rect.left < 64:
      musuhs.pop(index)
      print("markas di serang!!!")
   # colision musuh dan panah
   index_musuh = 0
   for peluru in musuhs:
      peluru_rect     = pygame.Rect(musuh.get_rect())
      peluru_rect.left= peluru[1]
      peluru_rect.top= peluru[2]
      if musuh_rect.colliderect(peluru_rect):
         score += 1
         musuhs.pop(index)
         panahs.pop(index_panah)
         print("Boom! mati kau!")
         print("Skor : {}".format(skor))
      index_panah += 1
   index += 1

   pygame.display.flip()

   for event in pygame.event.get():
      if event.type == pygame.QUIT:
         pygame.quit()
         exit(0)

   if event.type == pygame.MOUSEBUTTONDOWN:
      panahs.append([sudut, new_playerpos[0]+32,
                            new_playerpos[1]+32])

   if event.type == pygame.KEYDOWN: # true jika tombol ditekan
      if event.key == K_w:
         tombol["atas"] = True
      elif event.key == K_a:
         tombol["kiri"] = True
      elif event.key == K_s:
         tombol["bawah"] = True
      elif event.key == K_d:
         tombol["kanan"] = True
   if event.type == pygame.KEYUP: # false jika tombol dilepas
      if event.key == K_w:
         tombol["atas"] = False
      elif event.key == K_a:
         tombol["kiri"] = False
      elif event.key == K_s:
         tombol["bawah"] = False
      elif event.key == K_d:
         tombol["kanan"] = False

   if tombol["atas"]: # posisi Y
      playerpos[1] -= 5
   elif tombol["bawah"]:
      playerpos[1] += 5
   if tombol["kiri"]: # posisi X
      playerpos[0] -= 5 
   elif tombol["kanan"]:
      playerpos[0] += 5

