from register import register_user
import sys


if __name__ == "__main__":
  if len(sys.argv) < 2:
    print("REGISTER_FAILED")
    exit()
  
  email = sys.argv[1]

  succes = register_user(email)

  if succes:
    print("REGISTER_SUCCESS")
  else:
      print("REGISTER_FAILED")
