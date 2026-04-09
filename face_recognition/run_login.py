from login import login_user

if __name__ == "__main__":
  result = login_user()

  if result is not None:
    print(result)
  else:
    print("LOGIN_FAILED")