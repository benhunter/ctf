from secretsharing import SecretSharer
from secretsharing import PlaintextToHexSecretSharer

shares = [
        '1-0155345ac0739a5b76d982a11853e915a311fb6bde2ee880a93d2af5ac73f0a9c00ebdca6311ee981da0af0ce0c1ef733747111e513b7429e2ed2a2bfd14c761c2',
        '2-060e055ae7947c49b246fe0b6efef63a99087654198f0803d021392ba57ef17739ecf93d4a569b57f914a2a51e3afa1f180b61c3fe90790120c6ac57f287f09d6b',
        '3-101bf5f7242c0784ece38d91bd332a948582d3c27c9da35be9f95e47c17d2bd9d0994d43dac85d822148e0c4d0d1bc99515a4ee32d8fefbb6fb96ffdee068b251c',
        '4-2170892625039dc7614a4c86bc2289490c2076c0d1d7ff5b6c12cdefd6cac942e6d1117fbe2e541fc1f13637aa87a03f3109d237cc93872d51bb2d67c60b740779',
        '5-3bfd41de98e4a0cb4a16563d24ff157dd080c258e3bb60d4cbbabbc9bbc3f323df519d929e4f9e3a06c16fc95d5e0e6e04efe57dc9f4ee2b48c19cdf5111885326']

# print(shares[0:5])
print([shares[0], shares[1], shares[2], shares[3]])

p = PlaintextToHexSecretSharer.recover_secret(shares[0:4])
print(p)
print('---')
p = PlaintextToHexSecretSharer.recover_secret(shares[1:5])
print(p)
