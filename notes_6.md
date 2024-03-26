

Notes 6


Phase 6:
- 
java -jar client.jar localhost 8080
- websocket
  - added for new menu options, 
    - join game has to do websocket and HTTP

Seurity
- SHA-1: digests- git uses to calculate commit IDs
  - compact, unique summory or identify for some data (extrememly likely to be unique)
  - Bit coin mining
  - secure passwords
  - digital signitures
- Salt
  - random string we generate and add it to hashed password 
    - added before you hash the password
    - Bcript does all that for you
    - or you generate a random string append or prepend to password - then hash it
- Man in the middle attack -- someone else set up a fake front end and back end
- Cryptographic hash functions (SHA-5120) are designed to be time and memory-efficient to compute
- for password hashing, it is best to use a cryptographic hash function that is costly to copmute -- slow and/or uses lots of memory
  - bcrypt
  - scrypt
  - Argon2
Data Encryption
  - encoding data so that only authorized party can read it. Decryption makes it readable
    - PlainText - data you want to protect
    - Ciphertext - encrypted
    - Key - sequence of bits used as an input to the cryptographic algorithm to encode or decode data
    - Key Size, larger the more secure
  - Symmetic Key - secret Key
    - same key to encrypt and decrypt
      - not a very good way to share a key
  - public key encryption
    - two keys, special algorithm, one to encrypt, one to decrypt -- one way only
    - public key is for everyone to send you a private message - you only have the decrypt key
      - anyone can send you data, only you can read it
    - limited on how much data you can send - has to be shorter than the key, slower, private keys never shared
    - use public key encryption to decript symmetric key
Password Manager
- Stores all your passwords in an encrypted file
Secure Key Exchange
- large amounts of data - symmetric keys
- public key encryption
HTTPS - SSL
- diagram from slides
- certificate file = public key + key owner's identifying info
- Certificate Authorities - browsers trust these companies, let's encrypt goDaddy DigiCert Sectigo
- certificate allows you to speak SSL
Digital Signitures
- how to know that server's certificate actually came from a trusted certificate authority?
- applied to a file - 