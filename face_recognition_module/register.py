import face_recognition
import numpy as np
import time
import cv2
from storage import save_embedding

#pentru inrolare userul deschide camera, eu voi lua mai multe frame uri la distanta de 1.0s fiecare,
#si le trec prin filtre, cand am 3 frame uri care au trecut de filtre, extrag embeddings
#facand media lor, salvez si inchid camera
#pentru inrolare vom cere mai multe poze, vom extrage embeddings si vom face media 
# lor pe care o salvam in spate. Fiecare poza care va fi luata in considerare
#pentru embeddings va trece printr o serie de filtre
def check_frame_quality(frame,
                         min_face_size = 120,
                         margin = 20,
                         blur_threshold = 100.0,
                         min_brightness = 60,
                         max_brightness = 200):
  if frame is None:
    return False, "Frame invalid", None

  #open cv citeste BGR dar face_recognition are nevoie de RGB
  rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
  gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)


  #filtru 1: sa avem o singura fata in cadru
  face_locations = face_recognition.face_locations(rgb_frame)
  if len(face_locations) == 0:
    return False, "Nu a fost detectata nicio fata", None
  if len(face_locations) > 1:
    return False, "Sunt prea multe fete in cadru", None

  #preluam coordonatele fetelor ca sa facem filtrul2:
  #filtru2: persoana sa fie suficient de aproape/departe de camera
  top, right, bottom, left = face_locations[0]
  face_width = right - left
  face_height = bottom - top
  if face_width < min_face_size or face_height < min_face_size:
    return False, "Esti prea departe, apropie-te de camera", None

  h, w = frame.shape[:2] #frame e de forma (height, width, nr canale), eu am luat height si width
  if top < margin or left < margin or right > (w - margin) or bottom > (h - margin):
    return False, "Esti prea aproape de camera, indeparteaza-te", None

  #filtru 3: blur
  lap_var = cv2.Laplacian(gray, cv2.CV_64F).var() #cerem rezultat in format 64float pentru precizie mai buna, extragem varianta
  if lap_var < blur_threshold:
    return False, "Imaginea e prea blurata", None 

  #filtru 4: luminozitate
  mean_brightness = np.mean(gray)
  if mean_brightness < min_brightness:
    return False, "Imaginea e prea intunecata", None
  if mean_brightness > max_brightness:
    return False, "Imaginea e prea luminoasa", None

  #daca totul e in regula, returnam locatiile fetei
  return True, "OK", face_locations[0]

#functie extragere embedding
def extract_embedding_from_frame(frame, face_location):
  rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
  face_encodings = face_recognition.face_encodings(rgb_frame, [face_location])

  if len(face_encodings) == 0:
    return None

  return face_encodings[0]

#functie inrolare din camera, adica tinem camera deschisa pana avem 3 frame uri bune
def get_live_embedding(required_samples = 3, min_time_between_samples = 1.0, timeout_seconds = 30.0):
  cap = cv2.VideoCapture(0)

  if not cap.isOpened():
    print("Eroare: nu pot deschide camera")
    return None
  
  valid_embeddings = []
  last_capture_time = 0 
  start_time = time.time()
  print("Inrolarea a inceput. Stai in fata camerei!")

  while True:
    ret,frame = cap.read()
    if not ret:
      print("Eroare la citirea frame ului")
      break

    #Timeout:
    elapsed = time.time() - start_time
    if elapsed > timeout_seconds:
      print("Timeout: nu s au obtinut suficiente frame uri valide")
      break

    current_time = time.time() #delay intre capturi
    is_valid, message, face_location = check_frame_quality(frame)
    if is_valid and (current_time - last_capture_time >= min_time_between_samples): #luam frame uri la minim 1s distanta
      embedding = extract_embedding_from_frame(frame, face_location)
      if embedding is not None:
        valid_embeddings.append(embedding)
        last_capture_time = current_time
        print(f"Frame valid capturat: {len(valid_embeddings)}/{required_samples}")
        cv2.imshow("Enroll Face", frame)
        cv2.waitKey(1)

    if len(valid_embeddings) >= required_samples:
      break

  cap.release()
  cv2.destroyAllWindows()

  if len(valid_embeddings) < required_samples:
    return None

  final_embedding = np.mean(valid_embeddings, axis = 0) #face media pe coloane

  return final_embedding
    
## salvare in json
def register_user(email):
  final_embedding = get_live_embedding()

  if final_embedding is None:
    print("Inrolarea a esuat.")
    return False

  save_embedding(email, final_embedding)
  print("Inrolare reusita")

  return True