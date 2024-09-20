import hashlib
import random
import string
import base64
import sys

def generate_random_input(prefix, length=10):
    #Generates a random input string by appending a random suffix to the specified prefix."""
    suffix = ''.join(random.choices(string.ascii_letters + string.digits, k=length)) # used this in EA as well. 
    return prefix + suffix

def find_partial_sha256_collision(prefix, num_bytes=4, target_byte=b'\xAA'):
    #Finds two different inputs that result in SHA256 hashes with identical leading num_bytes where all bytes are the same value."""
    seen_hashes = {} # use of a dictionary to retain hashes we have seen before 
    attempts = 0

    while True:
       
        input_data = generate_random_input(prefix)# Generate a single random input string starting with the specified prefix

        digest = hashlib.sha256(input_data.encode()).digest()


        leading_bytes = digest[:num_bytes] # first extracts the leading bytes 

        
        if leading_bytes == target_byte * num_bytes: # check for leading bytes = target byte 
            # Check if we've already seen this hash prefix and ensure the inputs are different
            if leading_bytes in seen_hashes:
                other_input, other_digest = seen_hashes[leading_bytes]
                if input_data != other_input:
                    return input_data, other_input
            else:
                seen_hashes[leading_bytes] = (input_data, digest)
        
        attempts += 1

        # Output debug information to stderr every 100,000 attempts
        if attempts % 100000 == 0:
            print(f"Attempts: {attempts}", file=sys.stderr)

# Main 
if __name__ == "__main__":
    try:
        
        prefix = "cjm0170@auburn.edu"
        #email input above. 

        # Find the partial collision where the first 4 bytes are all the same (e.g., 0xAA)
        input1, input2 = find_partial_sha256_collision(prefix, num_bytes=4, target_byte=b'\xAA')

        
        base64_i1 = base64.b64encode(input1.encode()).decode()
        base64_i2 = base64.b64encode(input2.encode()).decode()

        # Output the Base64-encoded inputs to stdout
        print(f"INPUT 1 -- {base64_i1}")
        print(f"INPUT 2 -- {base64_i2}")

        # originally have the Disgest as part of the print to stdout, but really was just using it for debugging purposes. 

        sys.exit(0) # exit 0 

    except Exception as e:
        print(f"Error: {str(e)}", file=sys.stderr)
        sys.exit(1)
