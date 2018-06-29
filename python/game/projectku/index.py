import pygame
import math
from pygame.locals import *

pygame.init()
width, height = 640, 480
screen = pygame.display.set_mode((width, height))
run = True
pos  = [100, 100]
keys = {
    "top": False,
    "bottom": False,
    "left": False,
    "right": False
}

rumput = pygame.image.load("images/rumput.png")
tkiri  = pygame.image.load("images/tkiri.png")
badan  = pygame.image.load("images/badan.png")
kepala = pygame.image.load("images/kepala.png")
rambut = pygame.image.load("images/rambut.png")
kaki   = pygame.image.load("images/kaki.png")
tkanan = pygame.image.load("images/tkanan.png")

def tombol():
   if event.type == pygame.KEYDOWN:
      if event.key == K_q:
         keys["top"] = True
      elif event.key == K_z:
         keys["left"] = True
      elif event.key == K_a:
         keys["bottom"] = True
      elif event.key == K_x:
         keys["right"] = True
   if event.type == pygame.KEYUP:
      if event.key == K_q:
         keys["top"] = False
      elif event.key == K_z:
         keys["left"] = False
      elif event.key == K_a:
         keys["bottom"] = False
      elif event.key == K_x:
         keys["right"] = False

while (run):
   screen.fill(0)

   for x in range(int(width/rumput.get_width()+1)):
      for y in range(int(height/rumput.get_height()+1)):
         screen.blit(rumput, (x*100, y*100))

   screen.blit(tkiri , (pos[0]+40, pos[1]-42))
   screen.blit(badan , (pos[0]+0 , pos[1]-50))
   screen.blit(kepala, (pos[0]-10, pos[1]-125))
   screen.blit(rambut, (pos[0]-37, pos[1]-138))
   screen.blit(kaki  , (pos[0]+0 , pos[1]+0))

   mouse  = pygame.mouse.get_pos()
   sudut  = math.atan2(mouse[1]-(pos[1]+32), mouse[0]-(pos[0]+26))
   rotasi = pygame.transform.rotate(tkanan, 360-sudut*57.29)
   new_pos= (pos[0]-rotasi.get_rect().width/2, pos[1]-rotasi.get_rect().height/2)
   screen.blit(rotasi, new_pos)

   pygame.display.flip()

   for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            exit(0)
   tombol()

   if keys["top"]:
      pos[1] -= 5
   elif keys["bottom"]:
      pos[1] += 5
   if keys["left"]:
      pos[0] -= 5
   elif keys["right"]:
      pos[0] += 5
