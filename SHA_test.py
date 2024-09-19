import hashlib
import random
import string

def generate_random_input(prefix, length=10):
    """Generates a random input string with a specified prefix and random suffix of given length."""
    suffix = ''.join(random.choices(string.ascii_letters + string.digits, k=length))
    return prefix + suffix

def find_partial_sha256_collision(prefix, num_bytes=4):
    """Finds two different inputs that result in SHA256 hashes with identical leading num_bytes."""
    seen_hashes = {}
    attempts = 0

    while True:
        # Generate two random input strings
        input1 = generate_random_input(prefix)
        input2 = generate_random_input(prefix)

        # Ensure inputs are different
        if input1 == input2:
            continue

        # Calculate SHA256 hashes
        hash1 = hashlib.sha256(input1.encode()).digest()
        hash2 = hashlib.sha256(input2.encode()).digest()

        # Check if the first num_bytes of both hashes match
        if hash1[:num_bytes] == hash2[:num_bytes]:
            return input1, input2, hash1[:num_bytes], attempts
        
        attempts += 1

# Usage example
prefix = "cjm0170@auburn.edu"
num_bytes = 4  # We're looking for the first 4 bytes to match

input1, input2, matching_bytes, attempts = find_partial_sha256_collision(prefix, num_bytes)

print(f"Collision found after {attempts} attempts:")
print(f"Input 1: {input1}")
print(f"Input 2: {input2}")
print(f"Matching leading bytes: {matching_bytes.hex()}")