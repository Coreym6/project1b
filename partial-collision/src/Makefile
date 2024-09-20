# Target run when the shell command `make build` is run.
# There is nothing to build for Python so simply exit successfully.
build:
	@exit 0

# Target run when the shell command `make run FILE=XXXX` is run.
# The input file path is passed as the `XXXX` portion of the argument to make
# and is relayed to the Python script as it's first and only command line
# argument.
run:
	@if [ ! -f ../1-input.txt ]; then \
		touch ../1-input.txt; \
	fi
	@if [ ! -f ../2-input.txt ]; then \
		touch ../2-input.txt; \
	fi
	@python3 SHA_test.py $(FILE) | tee >(head -n 1 > ../1-input.txt) >(head -n 2 | tail -n 1 > ../2-input.txt)