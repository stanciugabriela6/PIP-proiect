import face_recognition
import numpy as np
import time
import cv2
from storage import save_embedding


def check_frame_quality(frame,
                        min_face_size=80,
                        margin=10,
                        blur_threshold=40.0,
                        min_brightness=40,
                        max_brightness=220):
    if frame is None:
        return False, "Frame invalid", None

    rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Filtru 1: exact o singura fata
    face_locations = face_recognition.face_locations(rgb_frame)

    if len(face_locations) == 0:
        return False, "Nu a fost detectata nicio fata", None

    if len(face_locations) > 1:
        return False, "Sunt prea multe fete in cadru", None

    top, right, bottom, left = face_locations[0]

    # Filtru 2: dimensiunea fetei
    face_width = right - left
    face_height = bottom - top

    if face_width < min_face_size or face_height < min_face_size:
        return False, "Esti prea departe, apropie-te de camera", None

    h, w = frame.shape[:2]

    if top < margin or left < margin or right > (w - margin) or bottom > (h - margin):
        return False, "Esti prea aproape de camera, indeparteaza-te", None

    # Filtru 3: blur
    face_roi = gray[top:bottom,left:right]
    lap_var = cv2.Laplacian(face_roi,cv2.CV_64F).var()

    if lap_var < blur_threshold:
        return False, f"Imaginea e prea blurata ({lap_var:.2f})",None

    # Filtru 4: luminozitate
    mean_brightness = np.mean(gray)

    if mean_brightness < min_brightness:
        return False, "Imaginea e prea intunecata", None

    if mean_brightness > max_brightness:
        return False, "Imaginea e prea luminoasa", None

    return True, "OK", face_locations[0]


def extract_embedding_from_frame(frame, face_location):
    rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    face_encodings = face_recognition.face_encodings(rgb_frame, [face_location])

    if len(face_encodings) == 0:
        return None

    return face_encodings[0]


def get_live_embedding(required_samples=3, min_time_between_samples=1.0, timeout_seconds=30.0):
    cap = cv2.VideoCapture(0)

    if not cap.isOpened():
        print("Eroare: nu pot deschide camera")
        return None

    valid_embeddings = []
    last_capture_time = 0
    start_time = time.time()

    print("Inrolarea a inceput. Stai in fata camerei!")

    while True:
        ret, frame = cap.read()
        if not ret:
            print("Eroare la citirea frame-ului")
            break

        elapsed = time.time() - start_time
        if elapsed > timeout_seconds:
            print("Timeout: nu s-au obtinut suficiente frame-uri valide")
            break

        current_time = time.time()
        is_valid, message, face_location = check_frame_quality(frame)

        # desenam dreptunghi pe fata daca exista
        if face_location is not None:
            top, right, bottom, left = face_location
            color = (0, 255, 0) if is_valid else (0, 0, 255)
            cv2.rectangle(frame, (left, top), (right, bottom), color, 2)

        # mesaj pe ecran
        text_color = (0, 255, 0) if is_valid else (0, 0, 255)
        cv2.putText(
            frame,
            message,
            (20, 40),
            cv2.FONT_HERSHEY_SIMPLEX,
            0.8,
            text_color,
            2
        )

        # afisam si cate sample-uri bune avem
        progress_text = f"Capturi bune: {len(valid_embeddings)}/{required_samples}"
        cv2.putText(
            frame,
            progress_text,
            (20, 80),
            cv2.FONT_HERSHEY_SIMPLEX,
            0.8,
            (255, 255, 0),
            2
        )

        cv2.imshow("Enroll Face", frame)

        key = cv2.waitKey(1) & 0xFF
        if key == 27:  # ESC
            print("Inrolare anulata de utilizator")
            break

        if is_valid and (current_time - last_capture_time >= min_time_between_samples):
            embedding = extract_embedding_from_frame(frame, face_location)
            if embedding is not None:
                valid_embeddings.append(embedding)
                last_capture_time = current_time
                print(f"Frame valid capturat: {len(valid_embeddings)}/{required_samples}")
            else:
                print("Nu s-a putut extrage embedding-ul, desi frame-ul parea valid")

        if len(valid_embeddings) >= required_samples:
            print("S-au obtinut suficiente frame-uri valide")
            break

    cap.release()
    cv2.destroyAllWindows()

    if len(valid_embeddings) < required_samples:
        return None

    final_embedding = np.mean(valid_embeddings, axis=0)
    return final_embedding


def register_user(email):
    final_embedding = get_live_embedding()

    if final_embedding is None:
        print("Inrolarea a esuat.")
        return False

    save_embedding(email, final_embedding)
    print("Inrolare reusita")
    return True