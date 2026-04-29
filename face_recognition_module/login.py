import numpy as np
from storage import load_users
from register import get_live_embedding


#vom compara embedding ul extras live cu embeddings urile din json, cea mai mica
#diferenta de embeddings va rezulta emailul userului care e in proces de login

def compute_difference(embedding1, embedding2):
  return np.linalg.norm(embedding1 - embedding2)


def find_best_match(live_embedding, threshold = 0.6, json_path = "users.json"):
  data = load_users(json_path)

  if len(data["users"]) == 0:
    print("Nu exista users salvati in fisier")
    return None
  best_email = None
  best_difference = float("inf")

  for user in data["users"]:
    saved_embedding = np.array(user["embedding"])
    difference = compute_difference(live_embedding, saved_embedding)

    if difference < best_difference:
      best_difference = difference
      best_email = user["email"]
  if best_difference < threshold:
    return best_email

  return None


def login_user():
  live_embedding = get_live_embedding(window_title="Autentificare - Priveste la camera")

  if live_embedding is None:
    print("Login esuat: nu s a putut obtine embedding ul")
    return None

  matched_email = find_best_match(live_embedding)

  if matched_email is None:
    print("Login esuat: utilizator nerecunoscut")
    return None

  print(f"Login reusit: {matched_email}")
  return matched_email
  