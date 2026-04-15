import os
import json

#modul de salvare/incarcare/cautare in/din json

def save_user(data, json_path = "users.json"): #scrie json ul
  with open(json_path, "w") as f:
    json.dump(data, f, indent = 4)

def load_users(json_path = "users.json"):
  if not os.path.exists(json_path):
    return {"users": []}
  
  with open(json_path, "r") as f:
    try:
      return json.load(f)
    except json.JSONDecodeError:
      return {"users": []}
  
def save_embedding(email, embedding, json_path = "users.json"):
  if embedding is None:
    print("Eroare: embedding invalid")
    return False
  
  data = load_users(json_path)

  embedding_list = embedding.tolist()

  user_found = False

  for user in data["users"]:
    if user["email"] == email:
      user["embedding"] = embedding_list
      user_found = True
      break
  
  if not user_found:
    data["users"].append({
      "email": email,
      "embedding": embedding_list
    })

  save_user(data,json_path)

  print(f"User salvat: {email}")
  return True
