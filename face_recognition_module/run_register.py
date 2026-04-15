from register import register_user

def run_register(email):
    success = register_user(email)
    return success
